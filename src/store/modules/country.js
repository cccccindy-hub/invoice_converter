// store/modules/countries.js
import { fetchCountries } from '../../api/util/country'; // Adjust path as needed

const state = {
  countries: [],
  loading: false,
  error: null,
};

const mutations = {
  setCountries(state, countries) {
    state.countries = countries;
  },
  setLoading(state, loading) {
    state.loading = loading;
  },
  setError(state, error) {
    state.error = error;
  },
};

const actions = {
  async fetchCountries({ commit }) {
    commit('setLoading', true);
    commit('setError', null);
    
    try {
      const data = await fetchCountries();
      commit('setCountries', data);
    } catch (error) {
      commit('setError', 'Failed to fetch countries.');
      console.error('Error fetching countries:', error);
    } finally {
      commit('setLoading', false);
    }
  },
};

const getters = {
  countries: (state) => state.countries,
  loading: (state) => state.loading,
  error: (state) => state.error,
};

export default {
  namespaced: true, // This is important for namespacing
  state,
  mutations,
  actions,
  getters,
};
