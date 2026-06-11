import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../../services/api';
import Navbar from '../../components/Navbar/Navbar';
import Sidebar from '../../components/Sidebar/Sidebar';
import FormBuilder from '../../components/FormBuilder/FormBuilder';

const CreateForm = () => {
  const navigate = useNavigate();
  const [saving, setSaving] = useState(false);

  const handleSave = async (formData) => {
    setSaving(true);
    try {
      // Validate form title
      if (!formData.title || formData.title.trim().length < 3) {
        alert('Form title must be at least 3 characters long');
        setSaving(false);
        return;
      }

      // Validate fields have labels
      for (const field of formData.fields) {
        if (!field.label || field.label.trim() === '') {
          alert('All fields must have a label');
          setSaving(false);
          return;
        }
      }

      // Create form with title and description only
      const formResponse = await api.post('/forms', {
        title: formData.title.trim(),
        description: ''
      });
      
      const formId = formResponse.data.id;
      
      // Add each field to the form
      const fieldTypeMap = {
        'text': 'TEXT',
        'email': 'EMAIL',
        'textarea': 'TEXTAREA',
        'select': 'DROPDOWN',
        'radio': 'RADIO',
        'checkbox': 'CHECKBOX',
        'date': 'DATE',
        'number': 'NUMBER'
      };
      
      for (const field of formData.fields) {
        await api.post(`/forms/${formId}/fields`, {
          fieldType: fieldTypeMap[field.type] || 'TEXT',
          label: field.label.trim(),
          required: field.required || false,
          options: field.options && field.options.length > 0 ? field.options.filter(o => o.trim() !== '').join(',') : null
        });
      }
      
      navigate('/dashboard');
    } catch (error) {
      console.error('Error saving form:', error);
      const validationErrors = error.response?.data?.validationErrors;
      const errorMessage = error.response?.data?.message || 
        (validationErrors && typeof validationErrors === 'object' ? Object.values(validationErrors).join(', ') : null) ||
        'Failed to save form. Please try again.';
      alert(errorMessage);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <div className="flex">
        <Sidebar />
        <main className="flex-1 p-8">
          <div className="mb-8">
            <h1 className="text-3xl font-bold text-gray-900 mb-2">Create Form</h1>
            <p className="text-gray-600">Build your custom form with our drag-and-drop builder</p>
          </div>

          <div className="bg-white rounded-xl shadow-sm p-8">
            {saving ? (
              <div className="flex items-center justify-center py-12">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
              </div>
            ) : (
              <FormBuilder onSave={handleSave} />
            )}
          </div>
        </main>
      </div>
    </div>
  );
};

export default CreateForm;
