import { IQueryOptions } from '@/interface';
export function getPageRowSeq(idx: number,queryOptions:IQueryOptions<any>){
    if(queryOptions.page){
        return ((queryOptions.page.pageNumber||0) - 1) * (queryOptions.page.pageCount||0) + idx + 1;
    }else{
        return idx + 1;
    }
}