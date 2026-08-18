import axios from "axios"
import type { PageParam } from "../types/global"
import type { TodoAdd, TodoModify } from "../types/todo"

export const API_SERVER_HOST = 'http://localhost:8080'

const prefix = `${API_SERVER_HOST}/api/todo` 

export const getOne = async (tno : number) => {
    const res = await axios.get(`${prefix}/${tno}`)
    return res.data
}

export const getList = async (pageParam : PageParam) => {
    const res = await axios.get(`${prefix}/list`,{params : pageParam})
    return res.data
}

export const postAdd = async (todoObj : TodoAdd) => {
    const res = await axios.post(`${prefix}/regist`, todoObj)
    return res.data
}

export const deleteOne = async (tno : number) => {
    const res = await axios.delete(`${prefix}/${tno}`)
    return res.data
}

export const putOne = async (todo : TodoModify) => {
    const res = await axios.put(`${prefix}/${todo.tno}`,todo)
    return res.data
}