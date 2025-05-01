document.addEventListener('DOMContentLoaded', function () {
    UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
    UE.Editor.prototype.getActionUrl = function (action) {
        console.log(action);
        if (action == 'uploadimage') {
            // Replace with your image upload endpoint
            return 'http://47.xxx.xx.xx:8080/upload/uploadImg';
        } else {
            return this._bkGetActionUrl.call(this, action);
        }
    };
});

