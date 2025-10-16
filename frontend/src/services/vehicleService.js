import axiosInstance from './axiosInstance';

// Vehicle Service
export const vehicleService = {
  // Get all vehicles
  getAllVehicles: async () => {
    const response = await axiosInstance.get('/vehicles');
    return response.data;
  },

  // Get available vehicles
  getAvailableVehicles: async () => {
    const response = await axiosInstance.get('/vehicles/available');
    return response.data;
  },

  // Get vehicle by ID
  getVehicleById: async (id) => {
    const response = await axiosInstance.get(`/vehicles/${id}`);
    return response.data;
  },

  // Create vehicle (Admin only)
  createVehicle: async (vehicleData) => {
    const response = await axiosInstance.post('/vehicles', vehicleData);
    return response.data;
  },

  // Update vehicle (Admin only)
  updateVehicle: async (id, vehicleData) => {
    const response = await axiosInstance.put(`/vehicles/${id}`, vehicleData);
    return response.data;
  },

  // Delete vehicle (Admin only)
  deleteVehicle: async (id) => {
    const response = await axiosInstance.delete(`/vehicles/${id}`);
    return response.data;
  },

  // Update vehicle availability (Admin only)
  updateAvailability: async (id, availability) => {
    const response = await axiosInstance.patch(
      `/vehicles/${id}/availability?availability=${availability}`
    );
    return response.data;
  },
};

export default vehicleService;
