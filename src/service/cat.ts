import { IItemCat } from "@/interface";
import { ApiHandle } from "@/tools";

export namespace catService{
    const apiHandle = new ApiHandle('/server/question');
    export async function list():Promise<IItemCat[]>{
        let res: any = await apiHandle.get('/cat/list');
        // console.log(res)
        return res;
    }
    export async function getTreeData():Promise<IItemCat[]>{
        let catList = await list();
        let rootNodes: IItemCat[] = [];
        for (let cat of catList) {
            if (!cat.parentId) {
                cat.childs = fetchChilds(cat, catList);
                rootNodes.push(cat);
            }
        }
        return rootNodes.sort((a, b) =>{
            return (a?.order||0) - (b?.order||0);
        });
    }
    function fetchChilds(parent: IItemCat, srcCats: IItemCat[]) {
        let childs:any[] = [];
        for (let cat of srcCats) {
            if (cat.parentId == parent.id) {
                childs.push(cat);
                cat.childs = fetchChilds(cat, srcCats);
            }
        }
        return childs.sort((a, b) =>{
            return (a?.order||0) - (b?.order||0);
        });
    }
    // export async function add(cat:IItemCat):Promise<any>{
    //     let res: any = await apiHandle.post({url:'/add',data:cat});
    //     // console.log(res)
    // }
    // export async function edit(cat:IItemCat):Promise<any>{
    //     let res: any = await apiHandle.post({url:'/edit',data:cat});
    //     // console.log(res)
    // }
    export async function move(id:number,newParentId:number):Promise<any>{
        let res: any = await apiHandle.post({url:'/move',data:{id:id,newParentId:newParentId}});
        // console.log(res)
    }
    export async function del(id:number):Promise<any>{
        let res: any = await apiHandle.post({url:'/del',data:{id:id}});
        // console.log(res)
    }
}