import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import bookingService from '../services/bookingService';
import vehicleService from '../services/vehicleService';
import LoadingSpinner from '../components/LoadingSpinner';
import Button from '../components/Button';

const Dashboard = () => {
  const { user, isAdmin } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [activeTab, setActiveTab] = useState('bookings');

  // Vehicle form state
  const [showVehicleForm, setShowVehicleForm] = useState(false);
  const [vehicleFormData, setVehicleFormData] = useState({
    vehicleType: 'CAR',
    brand: '',
    model: '',
    rentalPricePerDay: '',
    registrationNumber: '',
    yearOfManufacture: '',
    description: '',
    availability: true,
    numberOfSeats: '',
    fuelType: '',
    transmissionType: '',
  });

  useEffect(() => {
    fetchData();
  }, [activeTab]);

  const fetchData = async () => {
    try {
      setLoading(true);
      if (activeTab === 'bookings') {
        const data = isAdmin()
          ? await bookingService.getAllBookings()
          : await bookingService.getMyBookings();
        setBookings(data);
      } else if (activeTab === 'vehicles' && isAdmin()) {
        const data = await vehicleService.getAllVehicles();
        setVehicles(data);
      }
      setError('');
    } catch (err) {
      setError('Failed to load data. Please try again.');
      console.error('Error fetching data:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleVehicleFormChange = (e) => {
    setVehicleFormData({
      ...vehicleFormData,
      [e.target.name]: e.target.value,
    });
  };

  const handleAddVehicle = async (e) => {
    e.preventDefault();
    try {
      await vehicleService.createVehicle({
        ...vehicleFormData,
        rentalPricePerDay: parseFloat(vehicleFormData.rentalPricePerDay),
        yearOfManufacture: parseInt(vehicleFormData.yearOfManufacture),
        numberOfSeats: parseInt(vehicleFormData.numberOfSeats) || null,
      });
      setShowVehicleForm(false);
      setVehicleFormData({
        vehicleType: 'CAR',
        brand: '',
        model: '',
        rentalPricePerDay: '',
        registrationNumber: '',
        yearOfManufacture: '',
        description: '',
        availability: true,
        numberOfSeats: '',
        fuelType: '',
        transmissionType: '',
      });
      fetchData();
    } catch (err) {
      alert('Failed to add vehicle: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleDeleteVehicle = async (id) => {
    if (window.confirm('Are you sure you want to delete this vehicle?')) {
      try {
        await vehicleService.deleteVehicle(id);
        fetchData();
      } catch (err) {
        alert('Failed to delete vehicle');
      }
    }
  };

  const handleUpdateBookingStatus = async (id, status) => {
    try {
      await bookingService.updateBookingStatus(id, status);
      fetchData();
    } catch (err) {
      alert('Failed to update booking status');
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'CONFIRMED':
        return 'bg-green-100 text-green-800';
      case 'PENDING':
        return 'bg-yellow-100 text-yellow-800';
      case 'COMPLETED':
        return 'bg-blue-100 text-blue-800';
      case 'CANCELLED':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">
            {isAdmin() ? 'Admin Dashboard' : 'My Dashboard'}
          </h1>
          <p className="text-gray-600 mt-2">Welcome back, {user?.name}!</p>
        </div>

        {/* Tabs */}
        <div className="mb-6 border-b border-gray-200">
          <div className="flex space-x-8">
            <button
              onClick={() => setActiveTab('bookings')}
              className={`pb-4 px-1 border-b-2 font-medium text-sm ${
                activeTab === 'bookings'
                  ? 'border-primary-500 text-primary-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              {isAdmin() ? 'All Bookings' : 'My Bookings'}
            </button>
            {isAdmin() && (
              <button
                onClick={() => setActiveTab('vehicles')}
                className={`pb-4 px-1 border-b-2 font-medium text-sm ${
                  activeTab === 'vehicles'
                    ? 'border-primary-500 text-primary-600'
                    : 'border-transparent text-gray-500 hover:text-gray-700'
                }`}
              >
                Manage Vehicles
              </button>
            )}
          </div>
        </div>

        {loading ? (
          <LoadingSpinner size="lg" />
        ) : error ? (
          <div className="text-center text-red-600 py-8">{error}</div>
        ) : (
          <>
            {/* Bookings Tab */}
            {activeTab === 'bookings' && (
              <div className="bg-white rounded-lg shadow-md overflow-hidden">
                <div className="overflow-x-auto">
                  {bookings.length === 0 ? (
                    <div className="text-center py-12 text-gray-500">
                      No bookings found
                    </div>
                  ) : (
                    <table className="min-w-full divide-y divide-gray-200">
                      <thead className="bg-gray-50">
                        <tr>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                            Vehicle
                          </th>
                          {isAdmin() && (
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                              Customer
                            </th>
                          )}
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                            Dates
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                            Cost
                          </th>
                          <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                            Status
                          </th>
                          {isAdmin() && (
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                              Actions
                            </th>
                          )}
                        </tr>
                      </thead>
                      <tbody className="bg-white divide-y divide-gray-200">
                        {bookings.map((booking) => (
                          <tr key={booking.id}>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="font-medium text-gray-900">
                                {booking.vehicleBrand} {booking.vehicleModel}
                              </div>
                            </td>
                            {isAdmin() && (
                              <td className="px-6 py-4 whitespace-nowrap">
                                <div className="text-sm text-gray-900">
                                  {booking.customerName}
                                </div>
                              </td>
                            )}
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="text-sm text-gray-900">
                                {booking.startDate} to {booking.endDate}
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                ${booking.totalCost}
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <span
                                className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusColor(
                                  booking.status
                                )}`}
                              >
                                {booking.status}
                              </span>
                            </td>
                            {isAdmin() && (
                              <td className="px-6 py-4 whitespace-nowrap text-sm">
                                <select
                                  value={booking.status}
                                  onChange={(e) =>
                                    handleUpdateBookingStatus(booking.id, e.target.value)
                                  }
                                  className="border border-gray-300 rounded px-2 py-1"
                                >
                                  <option value="PENDING">PENDING</option>
                                  <option value="CONFIRMED">CONFIRMED</option>
                                  <option value="COMPLETED">COMPLETED</option>
                                  <option value="CANCELLED">CANCELLED</option>
                                </select>
                              </td>
                            )}
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  )}
                </div>
              </div>
            )}

            {/* Vehicles Tab (Admin Only) */}
            {activeTab === 'vehicles' && isAdmin() && (
              <div>
                <div className="mb-4">
                  <Button onClick={() => setShowVehicleForm(!showVehicleForm)}>
                    {showVehicleForm ? 'Cancel' : '+ Add New Vehicle'}
                  </Button>
                </div>

                {showVehicleForm && (
                  <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                    <h3 className="text-xl font-bold mb-4">Add New Vehicle</h3>
                    <form onSubmit={handleAddVehicle} className="grid grid-cols-2 gap-4">
                      <div>
                        <label className="block text-sm font-medium mb-2">Vehicle Type</label>
                        <select
                          name="vehicleType"
                          value={vehicleFormData.vehicleType}
                          onChange={handleVehicleFormChange}
                          className="w-full px-4 py-2 border rounded-md"
                          required
                        >
                          <option value="CAR">Car</option>
                          <option value="BIKE">Bike</option>
                          <option value="VAN">Van</option>
                        </select>
                      </div>
                      <div>
                        <label className="block text-sm font-medium mb-2">Brand</label>
                        <input
                          type="text"
                          name="brand"
                          value={vehicleFormData.brand}
                          onChange={handleVehicleFormChange}
                          className="w-full px-4 py-2 border rounded-md"
                          required
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium mb-2">Model</label>
                        <input
                          type="text"
                          name="model"
                          value={vehicleFormData.model}
                          onChange={handleVehicleFormChange}
                          className="w-full px-4 py-2 border rounded-md"
                          required
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium mb-2">Price per Day</label>
                        <input
                          type="number"
                          step="0.01"
                          name="rentalPricePerDay"
                          value={vehicleFormData.rentalPricePerDay}
                          onChange={handleVehicleFormChange}
                          className="w-full px-4 py-2 border rounded-md"
                          required
                        />
                      </div>
                      <div className="col-span-2">
                        <Button type="submit" variant="success">Add Vehicle</Button>
                      </div>
                    </form>
                  </div>
                )}

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                  {vehicles.map((vehicle) => (
                    <div key={vehicle.id} className="bg-white rounded-lg shadow-md p-6">
                      <h3 className="text-lg font-bold mb-2">
                        {vehicle.brand} {vehicle.model}
                      </h3>
                      <p className="text-gray-600 mb-2">{vehicle.vehicleType}</p>
                      <p className="text-primary-600 font-bold mb-4">
                        ${vehicle.rentalPricePerDay}/day
                      </p>
                      <div className="flex gap-2">
                        <Button
                          variant="danger"
                          size="sm"
                          onClick={() => handleDeleteVehicle(vehicle.id)}
                        >
                          Delete
                        </Button>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
