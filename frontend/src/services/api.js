import axios from 'axios';

// API base URL - points to backend
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Unauthorized - clear token and redirect to login
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  register: (userData) => api.post('/auth/register', userData),
  login: (credentials) => api.post('/auth/login', credentials),
  logout: () => api.post('/auth/logout'),
  getCurrentUser: () => api.get('/auth/me'),
};

// Forms API
export const formsAPI = {
  getAllForms: () => api.get('/forms'),
  getFormById: (id) => api.get(`/forms/${id}`),
  createForm: (formData) => api.post('/forms', formData),
  updateForm: (id, formData) => api.put(`/forms/${id}`, formData),
  deleteForm: (id) => api.delete(`/forms/${id}`),
  publishForm: (id) => api.post(`/forms/${id}/publish`),
  unpublishForm: (id) => api.post(`/forms/${id}/unpublish`),
};

// Form Fields API
export const formFieldsAPI = {
  getFieldsByFormId: (formId) => api.get(`/forms/${formId}/fields`),
  createField: (formId, fieldData) => api.post(`/forms/${formId}/fields`, fieldData),
  updateField: (formId, fieldId, fieldData) => api.put(`/forms/${formId}/fields/${fieldId}`, fieldData),
  deleteField: (formId, fieldId) => api.delete(`/forms/${formId}/fields/${fieldId}`),
  reorderFields: (formId, fieldIds) => api.put(`/forms/${formId}/fields/reorder`, { fieldIds }),
};

// Form Submissions API
export const submissionsAPI = {
  getSubmissionsByFormId: (formId) => api.get(`/forms/${formId}/submissions`),
  getSubmissionById: (formId, submissionId) => api.get(`/forms/${formId}/submissions/${submissionId}`),
  createSubmission: (formId, submissionData) => api.post(`/forms/${formId}/submissions`, submissionData),
  deleteSubmission: (formId, submissionId) => api.delete(`/forms/${formId}/submissions/${submissionId}`),
};

// Analytics API
export const analyticsAPI = {
  getFormStats: (formId) => api.get(`/analytics/forms/${formId}/stats`),
  getOverallStats: () => api.get('/analytics/overall'),
};

export { api };
export default api;
