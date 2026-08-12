export interface PageParam{
    page ? : string | number
    size ? : string | number
}

export interface UseCustomMoveReturn{
    moveToList : (PageParam? : PageParam) => void
    page:number
    size:number
}