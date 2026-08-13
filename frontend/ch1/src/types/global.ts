export interface PageParam{
    page ? : string | number
    size ? : string | number
}

export interface UseCustomMoveReturn{
    moveToList : (PageParam? : PageParam) => void
    moveToModify : (tno:number) => void
    moveToRead : (tno:number) => void
    page:number
    size:number
}

export interface PageRequestDTO{
    page:number
    size:number
}

export interface PageResponseDTO<T>{
    dtoList : T[]
    pageNumList : number[]
    pageRequestDTO : PageRequestDTO | null
    prev : boolean
    next : boolean
    totalCount : number
    prevPage : number
    nextPage : number
    totalPage : number
    current : number
}

export interface PageComponentProps<T>{
    listData : PageResponseDTO<T>
    movePage : ({page} : PageParam) => void
}