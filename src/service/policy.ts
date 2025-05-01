// api.js
import axios from 'axios';

export async function getCityList(time) {
    const url = `https://gongzige.com/policy/api/portal/get_citys/${time}`;

    try {
        const response = await axios.get(url);

        if (response.status !== 200) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return response.data;
    } catch (error) {
        console.error('Error fetching city list:', error.message);
        return null;
    }
}
export async function getpolicybyid(fid) {
    const url = `https://gongzige.com/policy/api/portal/get/${fid}`;

    try {
        const response = await axios.get(url);

        if (response.status !== 200) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return response.data;
    } catch (error) {
        console.error('Error fetching city list:', error.message);
        return null;
    }
}

export async function getexchangerate() {
    const url = 'https://api.exchangerate-api.com/v4/latest/USD';

    try {
        const response = await axios.get(url);

        if (response.status !== 200) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return response.data;
    } catch (error) {
        console.error('Error fetching city list:', error.message);
        return null;
    }
}

export async function gettallTax() {
    const url = 'https://gongzige.com/policy/api/portal/tax/all';

    try {
        const response = await axios.get(url);

        if (response.status !== 200) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return response.data;
    } catch (error) {
        console.error('Error fetching city list:', error.message);
        return null;
    }
}

export async function getEnCity() {
    const url = 'https://gongzige.com/policy/api/portal/district/all';

    try {
        const response = await axios.get(url);

        if (response.status !== 200) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return response.data.result;
    } catch (error) {
        console.error('Error fetching city list:', error.message);
        return null;
    }
}

