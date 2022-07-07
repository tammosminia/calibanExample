import caliban.CalibanError.ExecutionError
import caliban.Value.{EnumValue, StringValue}
import caliban.introspection.adt.{__EnumValue, __InputValue, __Type}
import caliban.schema.{ArgBuilder, PureStep, Schema, Step, Types}
import enumeratum.EnumEntry

import scala.reflect.ClassTag
import scala.util.Try

object CalibanUtils {
  //see https://github.com/ghostdogpr/caliban/issues/383
  //TODO: can this be better generalized to get this without supplying values?
  def gqlEnumSchema[E <: EnumEntry](name: String, values: Seq[E]): Schema[Any, E] = new Schema[Any, E] {
    override def toType(isInput: Boolean, isSubscription: Boolean): __Type =
      Types.makeEnum(
        Some(name),
        None,
        values.toList.map(v => __EnumValue(v.entryName, None, false, None)),
        None
      )
    override def resolve(value: E): Step[Any] = PureStep(StringValue(value.entryName))
  }

  def gqlEnumInput[E <: EnumEntry](name: String, values: Seq[E]): ArgBuilder[E] = {
    case EnumValue(s) =>
      values.find(_.entryName == s) match {
        case Some(e) => Right(e)
        case None    => Left(ExecutionError(s"$s is no $name"))
      }
    case other => Left(ExecutionError(s"Can't build a String from input $other"))
  }

  def gqlValueClassSchemaFrom[R, T: ClassTag, F](name: String, from: Schema[R, F], mapping: T => F): Schema[R, T] = {
    from.contramap[T](mapping).rename(name, Some(name))
  }

  def gqlValueClassInputFrom[T, F](from: ArgBuilder[F], mapping: F => Try[T]): ArgBuilder[T] =
    from.flatMap(f => mapping(f).toEither.left.map(e => ExecutionError(s"cannot parse $f", innerThrowable = Some(e))))

  implicit class SchemaFunctions[R, T](from: Schema[R, T]) {
    def addDescription(description: String): Schema[R, T] = new Schema[R, T] {
      override def optional: Boolean = from.optional
      override def arguments: List[__InputValue] = from.arguments
      override def toType(isInput: Boolean, isSubscription: Boolean): __Type = from.toType_(isInput, isSubscription).copy(description = Some(description))
      override def resolve(value: T): Step[R] = from.resolve(value)
    }

    def renameBoth(name: String): Schema[R, T] = from.rename(name, Some(name))
  }
}
