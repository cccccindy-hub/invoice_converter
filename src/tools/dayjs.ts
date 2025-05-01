import dayjs from "dayjs";
import duration from "dayjs/plugin/duration";
dayjs.extend(duration);
import "dayjs/locale/zh-cn";
dayjs.locale("zh-cn");
export default dayjs;