/// <reference types="vite/client" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  // eslint-disable-next-line @typescript-eslint/no-explicit-any, @typescript-eslint/ban-types
  const component: DefineComponent<{}, {}, any>
  export default component
}
interface ImportMetaEnv {
  /**
   * api
   */
  // readonly VITE_NODE_API: string
  // 更多环境变量...
}
interface ImportMeta {
  readonly env: ImportMetaEnv
}
