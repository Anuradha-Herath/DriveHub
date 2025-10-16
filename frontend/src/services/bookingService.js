import axiosInstance from './axiosInstance';

// Booking Service
export const bookingService = {
  // Create booking
  createBooking: async (bookingData) => {
    const response = await axiosInstance.post('/bookings', bookingData);
    return response.data;
  },

  // Get all bookings (Admin only)
  getAllBookings: async () => {
    const response = await axiosInstance.get('/bookings');
    return response.data;
  },

  // Get user's bookings
  getMyBookings: async () => {
    const response = await axiosInstance.get('/bookings/my-bookings');
    return response.data;
  },

  // Get booking by ID
  getBookingById: async (id) => {
    const response = await axiosInstance.get(`/bookings/${id}`);
    return response.data;
  },

  // Update booking status (Admin only)
  updateBookingStatus: async (id, status) => {
    const response = await axiosInstance.patch(
      `/bookings/${id}/status?status=${status}`
    );
    return response.data;
  },

  // Cancel booking
  cancelBooking: async (id) => {
    const response = await axiosInstance.delete(`/bookings/${id}`);
    return response.data;
  },
};

export default bookingService;
