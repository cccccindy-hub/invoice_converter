import Compressor from 'compressorjs';
export function compressor(file:any,options:{maxWidth?:number;}){
    return new Promise((resolve,reject) => {
        // compressorjs 默认开启 checkOrientation 选项
        // 会将图片修正为正确方向
        new Compressor(file, {
            maxWidth:options.maxWidth,
            success: resolve,
            error(err) {
                reject(err.message);
            },
        });
      });
}