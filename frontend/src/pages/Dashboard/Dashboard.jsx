import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../../services/api';
import Navbar from '../../components/Navbar/Navbar';
import Sidebar from '../../components/Sidebar/Sidebar';

const Dashboard = () => {
  const [forms, setForms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [sidebarOpen, setSidebarOpen] = useState(false);

  useEffect(() => {
    fetchForms();
  }, []);

  const fetchForms = async () => {
    try {
      const response = await api.get('/forms');
      setForms(response.data);
    } catch (error) {
      console.error('Error fetching forms:', error);
    } finally {
      setLoading(false);
    }
  };

  const deleteForm = async (formId) => {
    try {
      await api.delete(`/forms/${formId}`);
      setForms(forms.filter(form => form.id !== formId));
    } catch (error) {
      console.error('Error deleting form:', error);
    }
  };

  const togglePublish = async (formId) => {
    try {
      const form = forms.find(f => f.id === formId);
      if (form.isPublished) {
        await api.post(`/forms/${formId}/unpublish`);
        setForms(forms.map(f => f.id === formId ? { ...f, isPublished: false } : f));
      } else {
        await api.post(`/forms/${formId}/publish`);
        // Fetch the updated form data to get the publicUrl
        const response = await api.get(`/forms/${formId}`);
        setForms(forms.map(f => f.id === formId ? { ...f, isPublished: true, publicUrl: response.data.publicUrl } : f));
      }
    } catch (error) {
      console.error('Error toggling publish status:', error);
      alert('Failed to update publish status');
    }
  };

  const copyPublicLink = async (formId) => {
    try {
      const form = forms.find(f => f.id === formId);
      if (form.publicUrl) {
        const publicLink = `${window.location.origin}/public-form/${form.publicUrl}`;
        await navigator.clipboard.writeText(publicLink);
        alert('Link copied to clipboard!');
      }
    } catch (error) {
      console.error('Error copying link:', error);
      alert('Failed to copy link');
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <div className="flex">
        <Sidebar isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />
        <main className="flex-1 p-8">
          <button
            onClick={() => setSidebarOpen(true)}
            className="md:hidden mb-4 px-4 py-2 bg-primary-600 text-white rounded-lg"
          >
            ☰ Menu
          </button>
          <div className="mb-8">
            <h1 className="text-3xl font-bold text-gray-900 mb-2">Dashboard</h1>
            <p className="text-gray-600">Manage your forms and view responses</p>
          </div>

          <div className="mb-6">
            <Link
              to="/create-form"
              className="inline-flex items-center px-6 py-3 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition font-medium"
            >
              <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
              </svg>
              Create New Form
            </Link>
          </div>

          {loading ? (
            <div className="flex items-center justify-center py-12">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
            </div>
          ) : forms.length === 0 ? (
            <div className="bg-white rounded-xl shadow-sm p-12 text-center">
              <svg className="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
              <h3 className="text-xl font-semibold text-gray-900 mb-2">No forms yet</h3>
              <p className="text-gray-600 mb-4">Create your first form to get started</p>
              <Link
                to="/create-form"
                className="text-primary-600 hover:text-primary-700 font-medium"
              >
                Create a form →
              </Link>
            </div>
          ) : (
            <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
              {forms.map((form) => (
                <div key={form.id} className="bg-white rounded-xl shadow-sm p-6 hover:shadow-md transition">
                  <div className="flex items-start justify-between mb-3">
                    <h3 className="text-lg font-semibold text-gray-900">{form.title}</h3>
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                      form.isPublished 
                        ? 'bg-green-100 text-green-700' 
                        : 'bg-gray-100 text-gray-600'
                    }`}>
                      {form.isPublished ? 'Published' : 'Unpublished'}
                    </span>
                  </div>
                  <p className="text-sm text-gray-600 mb-4">
                    {form.description || 'No description'}
                  </p>
                  <div className="flex flex-wrap gap-2 mb-4">
                    <button
                      onClick={() => togglePublish(form.id)}
                      className={`flex-1 px-4 py-2 rounded-lg transition text-sm font-medium ${
                        form.isPublished
                          ? 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                          : 'bg-green-600 text-white hover:bg-green-700'
                      }`}
                    >
                      {form.isPublished ? 'Unpublish' : 'Publish'}
                    </button>
                    {form.isPublished && form.publicUrl && (
                      <button
                        onClick={() => copyPublicLink(form.id)}
                        className="px-4 py-2 bg-blue-50 text-blue-600 rounded-lg hover:bg-blue-100 transition text-sm font-medium"
                      >
                        Copy Link
                      </button>
                    )}
                  </div>
                  <div className="flex gap-2 pt-4 border-t border-gray-100">
                    <Link
                      to={`/form-responses/${form.id}`}
                      className="flex-1 text-center px-4 py-2 bg-blue-50 text-blue-600 rounded-lg hover:bg-blue-100 transition text-sm font-medium"
                    >
                      View Responses
                    </Link>
                    <button
                      onClick={() => deleteForm(form.id)}
                      className="px-4 py-2 bg-red-50 text-red-600 rounded-lg hover:bg-red-100 transition text-sm font-medium"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </main>
      </div>
    </div>
  );
};

export default Dashboard;
