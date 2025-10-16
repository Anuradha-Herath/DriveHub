import { useState, useEffect } from 'react';
import vehicleService from '../services/vehicleService';
import VehicleCard from '../components/VehicleCard';
import LoadingSpinner from '../components/LoadingSpinner';

const Vehicles = () => {
  const [vehicles, setVehicles] = useState([]);
  const [filteredVehicles, setFilteredVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filter, setFilter] = useState('ALL');
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchVehicles();
  }, []);

  useEffect(() => {
    applyFilters();
  }, [filter, searchTerm, vehicles]);

  const fetchVehicles = async () => {
    try {
      setLoading(true);
      const data = await vehicleService.getAllVehicles();
      setVehicles(data);
      setError('');
    } catch (err) {
      setError('Failed to load vehicles. Please try again later.');
      console.error('Error fetching vehicles:', err);
    } finally {
      setLoading(false);
    }
  };

  const applyFilters = () => {
    let filtered = [...vehicles];

    // Filter by type
    if (filter !== 'ALL') {
      filtered = filtered.filter(
        (vehicle) => vehicle.vehicleType?.toUpperCase() === filter
      );
    }

    // Filter by search term
    if (searchTerm) {
      filtered = filtered.filter(
        (vehicle) =>
          vehicle.brand?.toLowerCase().includes(searchTerm.toLowerCase()) ||
          vehicle.model?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    setFilteredVehicles(filtered);
  };

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 className="text-4xl font-bold text-center mb-8">Browse Vehicles</h1>

        {/* Filters */}
        <div className="bg-white rounded-lg shadow-md p-6 mb-8">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Search
              </label>
              <input
                type="text"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                placeholder="Search by brand or model..."
                className="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Vehicle Type
              </label>
              <select
                value={filter}
                onChange={(e) => setFilter(e.target.value)}
                className="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              >
                <option value="ALL">All Vehicles</option>
                <option value="CAR">Cars</option>
                <option value="BIKE">Bikes</option>
                <option value="VAN">Vans</option>
              </select>
            </div>
          </div>
        </div>

        {/* Results */}
        {loading ? (
          <LoadingSpinner size="lg" />
        ) : error ? (
          <div className="text-center text-red-600 py-8">{error}</div>
        ) : filteredVehicles.length === 0 ? (
          <div className="text-center text-gray-600 py-8">
            <p className="text-xl mb-2">No vehicles found</p>
            <p>Try adjusting your filters</p>
          </div>
        ) : (
          <>
            <p className="text-gray-600 mb-4">
              Showing {filteredVehicles.length} vehicle(s)
            </p>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {filteredVehicles.map((vehicle) => (
                <VehicleCard key={vehicle.id} vehicle={vehicle} />
              ))}
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default Vehicles;
