export interface ILogedUser {
    token: string;
}
export interface IAdminApiResponse {
    code: number;
    msg: string;
    [key: string]: any;
}

export interface IErInfo {
    "employerid": number;
    "employerno": string;
    "employername": string;
    "employerShortName": string;
    "employegOrgNo": string;
}
