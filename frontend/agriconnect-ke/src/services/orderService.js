import axios from "axios";

const API_URL = "http://localhost:8080/api/orders/";

export const getOrders = () => axios.get(API_URL);
export const createOrder = (order) => axios.post(API_URL, order);