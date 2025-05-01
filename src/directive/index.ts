import hasRole from './permission/hasRole'
import {hasPermiDirective} from './permission/hasPermi'
import copyText from './common/copyText'
import { App } from 'vue'

export default function directive(app:App<Element>){
  app.directive('hasRole', hasRole)
  app.directive('hasPermi', hasPermiDirective)
  app.directive('copyText', copyText as any)
}