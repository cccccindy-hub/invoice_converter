export const fetchCountries = async () => {
    try {
      const response = await fetch('https://restcountries.com/v3.1/all');
      const data = await response.json();
      return data.map(country => ({
        name: country.name.common,
        code: country.ccn3,
      }))
      .sort((a, b) => a.name.localeCompare(b.name));
    } catch (error) {
      throw error; // Rethrow the error to handle it later
    }
  };