// Utility to get extra attributes based on the table name
export function addExtraAttributeToForm(extraAttributeForm, form) {
    // Iterate over each attribute in extraAttributeForm
    for (const attribute of extraAttributeForm) {
      const attributeId = attribute.id.toString(); // Ensure attribute.id is a string
      //console.log("Before conversion:", attribute.id, typeof attributeId);
      if (attribute.type.toLowerCase() === "json") {
        // If attribute type is JSON, initialize with an empty object
        form.extraData = {
          ...form.extraData,
          [attributeId]: {}
        };
    
        // Populate children attributes with null values
        for (const childrenAttribute of attribute.children) {
          const childId = childrenAttribute.id.toString(); // Ensure childrenAttribute.id is a string
          form.extraData[attributeId] = {
            ...form.extraData[attributeId],
            [childId]: null
          };
        }
      } else {
        // For non-JSON types, set attribute with null value
        form.extraData = {
          ...form.extraData,
          [attributeId]: null
        };
      }
    }  
}

// Utility function to add extra attribute values to a form object
export function addExtraAttributeValueToForm(extraData, form) {
    for (const key in extraData) {
      if (extraData.hasOwnProperty(key)) {
        const value = extraData[key];
  
        // Check if the value is an object and not null
        if (typeof value === 'object' && value !== null) {
          // If it's a JSON object, handle nested assignments
          for (const childKey in value) {
            if (value.hasOwnProperty(childKey) && key in form.extraData && form.extraData[key] != null && childKey in form.extraData[key]) {
              // Assign the child value to the form
              form.extraData[key][childKey] = value[childKey];
            }
          }
        } else {
          // For non-object values, assign directly if key exists in form.extraData
          if (key in form.extraData) {
            form.extraData[key] = value;
          }
        }
      }
    }
  }

export function formatValue(type, cellValue) {
    if (cellValue == null) {
      return ""
    }
    else if (type == "number") {
      const num = Number(cellValue);
      if (Number.isInteger(num)) {
        return num // Return integer as is
      } else {
        return num.toFixed(2); // Format decimals to two decimal places
      }
    }
    else if (type == "date") {
      const date = new Date(cellValue);
      if (!isNaN(date)) {
        const d = new Date(date);
        const day = String(d.getDate()).padStart(2, '0');  // Ensure two digits for day
        const month = String(d.getMonth() + 1).padStart(2, '0');  // Ensure two digits for month (months are 0-indexed)
        const year = String(d.getFullYear()).slice(2);  // Get last two digits of the year
        return `${month}/${day}/${year}`;

        //return date.toISOString().split('T')[0]; // Format as yyyy-mm-dd
      }
    } 
    // Check if the value is a boolean
    else if (type == "boolean") {
      return cellValue ? 'Yes' : 'No'; // Convert to readable format
    }
    // Return the original value for strings
    return cellValue; 
  }

  export function getCurrentMonth () {
    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Ensure two digits
    //console.log(`${year}/${month}`);
    return `${year}/${month}`;
  };

  /**
 * Compare two dates in mm/dd/yy format.
 * 
 * @param {string} date1 - The first date in mm/dd/yy format.
 * @param {string} date2 - The second date in mm/dd/yy format.
 * @returns {number} - Returns:
 *  0 if the dates are equal,
 *  -1 if date1 is earlier than date2,
 *  1 if date1 is later than date2.
 */
export function compareDates(date1, date2) {//return true if date2 is greater(after) day
  if (date1 != null && date2 != null) {
    const parseDate = (date) => {
      const [month, day, year] = date.split('/').map(Number);
      const fullYear = year < 100 ? (year < 50 ? 2000 + year : 1900 + year) : year; // Handle yy as 2-digit year
      return new Date(fullYear, month - 1, day);
  };

    const d1 = parseDate(date1);
    const d2 = parseDate(date2);

    return d1.getTime() < d2.getTime() ? true : false;
  }
  else {
    return 0
  }
}

export function daysBetweenDates(date1, date2) {
  if (date1 != null && date2 != null) {
    const parseDate = (date) => {
      const [month, day, year] = date.split('/').map(Number);
      const fullYear = year < 100 ? (year < 50 ? 2000 + year : 1900 + year) : year; // Handle yy as 2-digit year
      return new Date(fullYear, month - 1, day);
    };

    const d1 = parseDate(date1);
    const d2 = parseDate(date2);

    // Calculate the difference in time (in milliseconds) and convert it to days
    const timeDifference = d2.getTime() - d1.getTime();
    const daysDifference = timeDifference / (1000 * 3600 * 24); // Convert milliseconds to days

    // Return the number of days, rounding it to the nearest integer
    return Math.floor(daysDifference);
  } else {
    return 0; // Return 0 if any date is invalid
  }
}

export function getCurrentDate() {
  const today = new Date();

  // Format the date as MM/DD/YYYY
  const month = (today.getMonth() + 1).toString().padStart(2, '0'); // Add leading zero if needed
  const day = today.getDate().toString().padStart(2, '0'); // Add leading zero if needed
  const year = today.getFullYear();

  return `${month}/${day}/${year}`;
}