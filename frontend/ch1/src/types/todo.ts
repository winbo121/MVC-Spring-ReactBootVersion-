export interface Todo {
    tno: number
    title: string
    writer: string
    dueDate: string | null
    complete: boolean
}

export interface TodoAdd{
    title :string
    writer: string
    dueDate: string 
    complete : boolean
}

export interface TodoModify{
    tno: number
    title :string
    writer: string
    dueDate: string | null
    complete : boolean
}