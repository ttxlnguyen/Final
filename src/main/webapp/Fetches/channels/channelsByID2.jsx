import React, { useState, useEffect } from 'react';

const ProfileDisplay = () => {
  // State to hold the fetched data
  const [data, setData] = useState(null);
  // loading state
  const [loading, setLoading] = useState(true);
  // handling any fetch errors
  const [error, setError] = useState(null);

  useEffect(() => {
    // creating a fetch data function
    const fetchData = async () => {
      try {
        const response = await fetch('http://localhost:8080/channels/1', {//our api endpoint
          headers: {//our bearer token to allow fetch
            Authorization: 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyNDk4NDk0MiwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzI0ODk4NTQyfQ.xffQj9bQh9rFQOJU8wrxauBNYNDyGHESffSVUUduYg4tcv84_P1NyFsKt0BvCUBmOQJrghsKp0vS2aTvpBtmAQ'
          }
        });
        
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        
        const result = await response.json();
        setData(result); // Updating state with the fetched data
      } catch (error) {
        setError(error); // Updating state with the error
      } finally {
        setLoading(false); // Set loading to false when done
      }
    };

    fetchData(); // Calling fetch function
  }, []); // Empty dependency array means this runs once when the component mounts

  // Conditionals for any loading or state errors
  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error.message}</p>;
  if (!data) return <p>No data available</p>;

  // Extracting what data we want to display
  const { channels, messages } = data;
  
  return (
    <div>
      <p> {channels} Messages: {messages}</p>
    </div>
  );
};

export default ProfileDisplay;
