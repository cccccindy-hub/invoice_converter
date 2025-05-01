import {PayApiHandler} from "@/tools/PayApiHandle";

export namespace MessageApi {

    const apiHandle = new PayApiHandler('/sys/message');

    export async function messageList(pageForm) {

       return apiHandle.get("/page", pageForm);
    }

    export async function viewMessage(msgId) {

        return apiHandle.get(`/view/${msgId}`)
    }

    export async function updateMsg(msg) {

        return apiHandle.put({url: "/update", data: msg});
    }

    export async function createMsg(msg) {

        return apiHandle.post({url: "/create", data: msg});
    }

    export async function publish(msgId) {

        return apiHandle.put({url:`/publish/${msgId}`})
    }

    export async function del(msgId) {

        return apiHandle.delete(`/del/${msgId}`)
    }

    export async function finish(msgId) {

        return apiHandle.put({url: `/finish/${msgId}`})
    }
}