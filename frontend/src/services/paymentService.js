import axiosInstance from './axiosInstance';

// Payment Service
export const paymentService = {
  // Process payment
  processPayment: async (paymentData) => {
    const response = await axiosInstance.post('/payments/process', paymentData);
    return response.data;
  },
};

export default paymentService;
