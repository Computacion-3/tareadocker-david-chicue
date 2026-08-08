import React from 'react';
import useStomp from '../../hooks/useStomp';

interface RealTimeWrapperProps {
  children: React.ReactNode;
}

const RealTimeWrapper: React.FC<RealTimeWrapperProps> = ({ children }) => {
  // Initialize the STOMP connection
  useStomp();

  return <>{children}</>;
};

export default RealTimeWrapper;
