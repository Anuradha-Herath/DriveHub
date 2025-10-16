import { Link } from 'react-router-dom';

const VehicleCard = ({ vehicle }) => {
  const getVehicleIcon = (type) => {
    switch (type?.toUpperCase()) {
      case 'CAR':
        return '🚗';
      case 'BIKE':
        return '🏍️';
      case 'VAN':
        return '🚐';
      default:
        return '🚗';
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300">
      <div className="h-48 bg-gradient-to-br from-primary-400 to-primary-600 flex items-center justify-center">
        <span className="text-6xl">{getVehicleIcon(vehicle.vehicleType)}</span>
      </div>
      <div className="p-6">
        <div className="flex items-center justify-between mb-2">
          <h3 className="text-xl font-bold text-gray-800">
            {vehicle.brand} {vehicle.model}
          </h3>
          <span
            className={`px-2 py-1 rounded-full text-xs font-semibold ${
              vehicle.availability
                ? 'bg-green-100 text-green-800'
                : 'bg-red-100 text-red-800'
            }`}
          >
            {vehicle.availability ? 'Available' : 'Not Available'}
          </span>
        </div>
        <p className="text-gray-600 text-sm mb-4 line-clamp-2">
          {vehicle.description || 'No description available'}
        </p>
        <div className="flex items-center justify-between mb-4">
          <div>
            <span className="text-2xl font-bold text-primary-600">
              ${vehicle.rentalPricePerDay}
            </span>
            <span className="text-gray-500 text-sm">/day</span>
          </div>
          <span className="text-sm text-gray-500 bg-gray-100 px-3 py-1 rounded-full">
            {vehicle.vehicleType}
          </span>
        </div>
        <div className="flex gap-2">
          <Link
            to={`/vehicles/${vehicle.id}`}
            className="flex-1 bg-primary-600 hover:bg-primary-700 text-white text-center py-2 rounded-md transition font-medium"
          >
            View Details
          </Link>
          {vehicle.availability && (
            <Link
              to={`/booking/${vehicle.id}`}
              className="flex-1 bg-green-600 hover:bg-green-700 text-white text-center py-2 rounded-md transition font-medium"
            >
              Book Now
            </Link>
          )}
        </div>
      </div>
    </div>
  );
};

export default VehicleCard;
