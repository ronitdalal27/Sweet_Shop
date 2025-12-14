import api from "./axios"

export const getAllSweets = () => api.get("/sweets")

export const purchaseSweet = (id) =>
  api.post(`/sweets/${id}/purchase`)

export const addSweet = (data) =>
  api.post("/sweets", data)

export const deleteSweet = (id) =>
  api.delete(`/sweets/${id}`)

export const restockSweet = (id, quantity) =>
  api.post(`/sweets/${id}/restock?quantity=${quantity}`)

export const searchSweets = (params) =>
  api.get("/sweets/search", { params })
