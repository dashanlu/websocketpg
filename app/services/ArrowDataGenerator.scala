package services

import java.io.ByteArrayOutputStream
import java.util.Collections

import actor.Person
import akka.util.ByteString
import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.vector.dictionary.DictionaryProvider
import org.apache.arrow.vector.ipc.ArrowStreamWriter
import org.apache.arrow.vector.types.pojo.{ArrowType, Field, FieldType, Schema}
import org.apache.arrow.vector.util.Text
import org.apache.arrow.vector.{TinyIntVector, VarCharVector, VectorSchemaRoot}

import scala.util.Random

trait ArrowDataGenerator {
  def generate(): ByteString
}

class PersonArrowDataGenerator extends ArrowDataGenerator {

  import collection.JavaConverters._

  private val allocator = new RootAllocator(Integer.MAX_VALUE)
  private val personNameFieldType = new FieldType(false, new ArrowType.Utf8(), null)
  private val personAgeFieldType = new FieldType(false, new ArrowType.Int(8, true), null)

  private val personNameField: Field = new Field("name", personNameFieldType, Collections.emptyList())
  private val personAgeField: Field = new Field("age", personAgeFieldType, Collections.emptyList())
  private val personSchema: Schema = new Schema(List(personNameField, personAgeField).asJava)

  val random = Random

  private val persons = List(
    Person("fo", 20),
    Person("foo", 21),
    Person("fooo", 22),
    Person("foooo", 23),
    Person("fooooo", 24)
  )

  override def generate(): ByteString = {
    val candidate = persons(random.nextInt(5))
    val vectorSchemaRoot = VectorSchemaRoot.create(personSchema, allocator)
    val provider = new DictionaryProvider.MapDictionaryProvider

    val personNameFieldVector = vectorSchemaRoot.getVector("name")
    personNameFieldVector.asInstanceOf[VarCharVector].setSafe(0, new Text(candidate.name))
    personNameFieldVector.setValueCount(1)

    val personAgeFieldVector = vectorSchemaRoot.getVector("age")
    personAgeFieldVector.asInstanceOf[TinyIntVector].setSafe(0, candidate.age)
    personAgeFieldVector.setValueCount(1)

    val byteArrayOutputStream = new ByteArrayOutputStream()
    val arrowStreamWriter = new ArrowStreamWriter(vectorSchemaRoot, provider, byteArrayOutputStream)
    arrowStreamWriter.writeBatch()
    arrowStreamWriter.close()
    byteArrayOutputStream.close()
    ByteString(byteArrayOutputStream.toByteArray)
  }
}