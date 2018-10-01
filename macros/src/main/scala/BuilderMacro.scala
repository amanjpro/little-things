package me.amanj


import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context
import scala.annotation.{StaticAnnotation, compileTimeOnly}


object LittleThings {
  @compileTimeOnly("Enable macro paradise to components")
  class builder extends StaticAnnotation {
    def macroTransform(annottees: Any*): Any = macro BuilderMacroImpl.builderMacro
  }
}


object BuilderMacroImpl {
  def builderMacro(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    import c.universe.Flag._

    val inputs = annottees.map(_.tree).toList
    val expandee = inputs match {
      case (clazz: ClassDef) :: rest if ! clazz.mods.hasFlag(TRAIT | ABSTRACT) =>
        val clazzName = clazz.name.toTypeName
        val builderName = TypeName(s"${clazzName.toString}Builder")
        val (fields, setters) = clazz.impl.body.flatMap {
          case vdef: ValDef  if vdef.mods.hasFlag(PARAMACCESSOR) =>
            val setterName = TermName(s"set${vdef.name.toString.capitalize}")
            val fieldName = TermName(s"_${vdef.name.toString}")
            val fieldType = vdef.tpt
            Some((q"private var $fieldName: $fieldType = _",
              q"def ${setterName}(x: $fieldType): $builderName = { self.$fieldName = x; this } "))
          case _                          =>
            None
        }.unzip


        val build =
          q"""
            def build(): $clazzName =
              new $clazzName(
                ..${
                  fields.map {
                    case f: ValDef => q"${TermName(f.name.toString.tail)} = self.${f.name.toTermName}"
                  }
                }
              )
           """

        val builderClass =
          q"""
            final class $builderName {
              self =>
              ..$fields
              ..$setters
              $build
            }
          """

        val companion = rest match {
          case (obj: ModuleDef):: _ =>
            q"""
               ${obj.mods} object ${obj.name} {
                  ..${obj.impl.body.filter {
                    case d: DefDef => d.name != nme.CONSTRUCTOR
                    case _         => true
                  }}
                  $builderClass
               }
             """
          case _ =>
            q"""
              object ${clazzName.toTermName} {
                $builderClass
              }
            """
        }

        val result = q"""
          $clazz
          $companion
         """

         println(result)

         result
      case _ =>
        c.abort(c.enclosingPosition,
          "Only traits/classes can have builders")
        EmptyTree
    }
    c.Expr[Any](expandee)
  }
}

