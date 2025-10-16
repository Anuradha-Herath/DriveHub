const LoadingSpinner = ({ size = 'md', color = 'primary' }) => {
  const sizes = {
    sm: 'h-8 w-8',
    md: 'h-12 w-12',
    lg: 'h-16 w-16',
  };

  const colors = {
    primary: 'border-primary-600',
    white: 'border-white',
    gray: 'border-gray-600',
  };

  return (
    <div className="flex justify-center items-center">
      <div
        className={`${sizes[size]} ${colors[color]} border-4 border-t-transparent rounded-full animate-spin`}
      ></div>
    </div>
  );
};

export default LoadingSpinner;
