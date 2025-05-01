import { EditorView, MatchDecorator, Decoration, WidgetType, ViewPlugin, DecorationSet, ViewUpdate } from "@codemirror/view"
import { itemPattern } from "./tools";
import { IItem } from "@/interface";

/**
 * 创建自己的扩展
 */
export function placeholders(originItems:IItem[]) {

    /**
     * 将项目id替换为包含项目名称的html元素
     */
    class PlaceholderWidget extends WidgetType {
        constructor(readonly itemStr: string, readonly pos: number) { super() }
        eq(other: PlaceholderWidget) { return this.itemStr == other.itemStr }
        toDOM(view: EditorView) {
            let [itemId, type] = this.itemStr.split(".");
            const item = originItems.find(item => item.id == itemId||item.code == itemId);
            let elt = document.createElement("div");
            elt.style.cssText = `
            border: 1px solid blue;
            border-radius: 4px;
            padding: 3px;
            color: #333;
            padding: 2px;
            margin: 1px 3px;
            display: inline-block;
            background: #fff;`;
            if(item){
                elt.innerText = item?.name as string;
                elt.title=item?.code as string;
            }else{
                elt.innerText = itemId;
                elt.style.color='#fff';
                elt.style.backgroundColor = "red";
                elt.title='无此项目';
            }
            // if (type == "pp") itemName += ":上期";
            return elt
        }
        // updateDOM(dom: HTMLElement, view: EditorView){
        //     console.log("updateDOM",dom);
        //     return false;
        // }
    }
    const placeholderMatcher = new MatchDecorator({
        regexp: itemPattern,
        decoration: (match, view, pos) => Decoration.replace({
            widget: new PlaceholderWidget(match[1], pos),
        })
    });

    const placeholders = ViewPlugin.fromClass(class {
        placeholders: DecorationSet
        constructor(view: EditorView) {
            this.placeholders = placeholderMatcher.createDeco(view)
        }
        update(update: ViewUpdate) {
            this.placeholders = placeholderMatcher.updateDeco(update, this.placeholders)
        }
    }, {
        decorations: instance => instance.placeholders,
        provide: plugin => EditorView.atomicRanges.of(view => {
            return view.plugin(plugin)?.placeholders || Decoration.none
        })
    });
    return placeholders;
}