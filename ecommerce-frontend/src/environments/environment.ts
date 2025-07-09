// Environment configuration
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};

// Production environment configuration
export const environmentProd = {
  production: true,
  apiUrl: 'https://your-backend-url.com/api' // TODO: Replace with your actual backend URL after deployment
};

// Function to get the appropriate environment
export const getEnvironment = () => {
  // For production deployment, use production environment
  if (typeof window !== 'undefined') {
    const hostname = window.location.hostname;
    if (hostname !== 'localhost' && hostname !== '127.0.0.1') {
      return environmentProd;
    }
  }
  return environment;
}; 