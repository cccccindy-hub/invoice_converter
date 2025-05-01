export class TestScriptRunner {
    constructor(private script: string) {

    }
    private options = new Proxy({}, {
        /**
         * 拦截所有属性和方法的调用，均返回0
         * @param target 
         * @param property 
         * @returns 
         */
        get: function (target: any, property: string) {
            console.log(`Calling method '${property}'`,target);
            return ()=>0;
        }
    });
    async run() {
        let self = this;

        const func = eval(`async (self.options)=>{${this.script} }`);
        // let result = eval(`(()=>{ ${ str } })()`).bind(this);
        const result: any = await func(this.options);

        // let func = eval(`(self.options)=>{ ${this.script} }`);
        // // let result = eval(`(()=>{ ${ str } })()`).bind(this);
        // let result = func(self);
        console.log(result);
        if(result===undefined) throw new Error("脚本必须return值");
    }
}