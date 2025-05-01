export const ServiceType = [
    { id: 0, name: "EOR" },
    { id: 1, name: "Payroll" },
    { id: 2, name: "PEO" },
];

export const CommercialInsuranceScheme = [
    { name: 'No', money: 0 },
    { name: 'Basic Level', money: 217 },
    { name: 'Standard Level', money: 250 },
    { name: 'Advanced Level', money: 280 },
    { name: 'Premium Level', money: 450 },
];

export const ServiceFeeType = [
    { id: 0, name: "%" },
    { id: 1, name: "Flat" },
];
export const EmployerLiabilityInsurance = [
    { beo: 'Yes', bfb: 0.008 },
    { beo: 'No', bfb: 0 },
];
export const WithAUnion = [
    { beo: 'Yes', bfb: 0.02 },
    { beo: 'No', bfb: 0 },
];