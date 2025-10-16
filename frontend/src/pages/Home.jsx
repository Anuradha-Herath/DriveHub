import { Link } from 'react-router-dom';
import { useState, useEffect } from 'react';
import vehicleService from '../services/vehicleService';
import VehicleCard from '../components/VehicleCard';
import LoadingSpinner from '../components/LoadingSpinner';

const Home = () => {
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchAvailableVehicles();
  }, []);

  const fetchAvailableVehicles = async () => {
    try {
      setLoading(true);
      const data = await vehicleService.getAvailableVehicles();
      setVehicles(data.slice(0, 6)); // Show only 6 vehicles on home page
      setError('');
    } catch (err) {
      setError('Failed to load vehicles. Please try again later.');
      console.error('Error fetching vehicles:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero Section */}
      <div className="bg-gradient-to-r from-primary-600 to-primary-800 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24">
          <div className="text-center">
            <h1 className="text-5xl font-bold mb-6">
              Welcome to DriveHub
            </h1>
            <p className="text-xl mb-8 text-primary-100">
              Your trusted partner for vehicle rentals. Find the perfect ride for your journey.
            </p>
            <div className="flex justify-center gap-4">
              <Link
                to="/vehicles"
                className="bg-white text-primary-600 hover:bg-primary-50 px-8 py-3 rounded-lg font-semibold transition"
              >
                Browse Vehicles
              </Link>
              <Link
                to="/register"
                className="bg-primary-500 hover:bg-primary-400 text-white px-8 py-3 rounded-lg font-semibold transition border-2 border-white"
              >
                Get Started
              </Link>
            </div>
          </div>
        </div>
      </div>

      {/* Features Section */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-16">
          <div className="text-center">
            <div className="text-5xl mb-4">🚗</div>
            <h3 className="text-xl font-bold mb-2">Wide Selection</h3>
            <p className="text-gray-600">
              Choose from cars, bikes, and vans to suit your needs
            </p>
          </div>
          <div className="text-center">
            <div className="text-5xl mb-4">💰</div>
            <h3 className="text-xl font-bold mb-2">Best Prices</h3>
            <p className="text-gray-600">
              Competitive rates with transparent pricing
            </p>
          </div>
          <div className="text-center">
            <div className="text-5xl mb-4">🔒</div>
            <h3 className="text-xl font-bold mb-2">Secure Booking</h3>
            <p className="text-gray-600">
              Safe and secure online booking system
            </p>
          </div>
        </div>

        {/* Featured Vehicles */}
        <div className="mb-8">
          <h2 className="text-3xl font-bold text-center mb-8">
            Featured Vehicles
          </h2>
          {loading ? (
            <LoadingSpinner size="lg" />
          ) : error ? (
            <div className="text-center text-red-600 py-8">{error}</div>
          ) : vehicles.length === 0 ? (
            <div className="text-center text-gray-600 py-8">
              No vehicles available at the moment
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {vehicles.map((vehicle) => (
                <VehicleCard key={vehicle.id} vehicle={vehicle} />
              ))}
            </div>
          )}
        </div>

        {vehicles.length > 0 && (
          <div className="text-center">
            <Link
              to="/vehicles"
              className="inline-block bg-primary-600 hover:bg-primary-700 text-white px-8 py-3 rounded-lg font-semibold transition"
            >
              View All Vehicles
            </Link>
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;
