// import { TestScriptRunner } from "./TestScriptRunner";

/**
 * 产生一个随机数
 * @param min 
 * @param max 
 */
function getRandomInt(min: number, max: number) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min)) + min; //不含最大值，含最小值
}
/**
 * 识别脚本中的项目
 */
export const itemPattern = /\<@(.*?)@\>/g;
export function covert2ItemExp(idOrCode:string):string {
    return "<@"+idOrCode+"@>";
}

/**
 * 验证脚本并试算
 * @param script 
 */
export function validScript(script: string=""):{isValid:boolean, itemIds?:string[]} {
    console.log(script)
    try {
        // 提取脚本中的项目id，用于设置项目配置中的formula.itemIds,这里只能提取显示设置的项目的id，像考勤和纳税这种通过条件判断才能知道用哪些项目，这里是无法获取的
        let itemIds = new Set<string>();
        // 将脚本中的项目id替换为随机数
        if (script!==null){
            const replacedString = script.replace(itemPattern, (match, variable) => {
                //   console.log(999,match, variable)
                // 替换值得同时把id放入Set中
                itemIds.add(variable);
                return getRandomInt(1, 100).toString();
            });
        }
        // let runner = new TestScriptRunner(replacedString);
        // runner.run();
        return {
            isValid:true,
            itemIds:Array.from(itemIds)
        };
    } catch (error) {
        console.error(error)
        return {
            isValid:false
        };
    }
}