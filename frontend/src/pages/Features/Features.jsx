import { Link } from 'react-router-dom';
import Navbar from '../../components/Navbar/Navbar';

const Features = () => {
  const features = [
    {
      icon: '📝',
      title: 'Easy Form Builder',
      description: 'Create forms in minutes with our intuitive drag-and-drop builder. No coding required.',
    },
    {
      icon: '📊',
      title: 'Real-time Analytics',
      description: 'Track responses as they come in with powerful analytics and visualization tools.',
    },
    {
      icon: '🎨',
      title: 'Customizable Design',
      description: 'Match your brand with custom themes, colors, and styling options.',
    },
    {
      icon: '🔒',
      title: 'Secure & Private',
      description: 'Enterprise-grade security with SSL encryption and GDPR compliance.',
    },
    {
      icon: '📱',
      title: 'Mobile Friendly',
      description: 'Forms work perfectly on all devices - desktop, tablet, and mobile.',
    },
    {
      icon: '🔗',
      title: 'Easy Sharing',
      description: 'Share forms via link, embed on your website, or send via email.',
    },
    {
      icon: '📧',
      title: 'Email Notifications',
      description: 'Get instant email alerts when new responses are submitted.',
    },
    {
      icon: '📥',
      title: 'Export Data',
      description: 'Download responses as CSV, Excel, or PDF for further analysis.',
    },
    {
      icon: '🤝',
      title: 'Team Collaboration',
      description: 'Work together with your team to build and manage forms.',
    },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <Navbar />
      <div className="container mx-auto px-4 py-16">
        <div className="text-center mb-16">
          <h1 className="text-5xl font-bold text-gray-900 mb-4">
            Powerful Features
          </h1>
          <p className="text-xl text-gray-600">
            Everything you need to create, manage, and analyze forms
          </p>
        </div>

        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
          {features.map((feature, index) => (
            <div key={index} className="bg-white p-8 rounded-xl shadow-lg hover:shadow-xl transition">
              <div className="text-4xl mb-4">{feature.icon}</div>
              <h3 className="text-xl font-semibold mb-2 text-gray-900">
                {feature.title}
              </h3>
              <p className="text-gray-600">{feature.description}</p>
            </div>
          ))}
        </div>

        <div className="mt-16 text-center">
          <h2 className="text-3xl font-bold text-gray-900 mb-4">
            Ready to get started?
          </h2>
          <p className="text-gray-600 mb-8">
            Join thousands of users who trust our platform
          </p>
          <a
            href="/register"
            className="inline-block px-8 py-3 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition font-medium"
          >
            Start Free Trial
          </a>
        </div>
      </div>
    </div>
  );
};

export default Features;
