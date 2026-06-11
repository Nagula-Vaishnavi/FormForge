import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute/ProtectedRoute';
import Home from './pages/Home/Home';
import Login from './pages/Login/Login';
import Register from './pages/Register/Register';
import Dashboard from './pages/Dashboard/Dashboard';
import CreateForm from './pages/CreateForm/CreateForm';
import FormResponses from './pages/FormResponses/FormResponses';
import Features from './pages/Features/Features';
import PublicForm from './pages/PublicForm/PublicForm';
import MyForms from './pages/MyForms/MyForms';
import Analytics from './pages/Analytics/Analytics';
import Settings from './pages/Settings/Settings';
import './styles/index.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/features" element={<Features />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/create-form"
            element={
              <ProtectedRoute>
                <CreateForm />
              </ProtectedRoute>
            }
          />
          <Route
            path="/form-responses/:id"
            element={
              <ProtectedRoute>
                <FormResponses />
              </ProtectedRoute>
            }
          />
          <Route
            path="/analytics"
            element={
              <ProtectedRoute>
                <Analytics />
              </ProtectedRoute>
            }
          />
          <Route
            path="/settings"
            element={
              <ProtectedRoute>
                <Settings />
              </ProtectedRoute>
            }
          />
          <Route path="/public-form/:publicUrl" element={<PublicForm />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
