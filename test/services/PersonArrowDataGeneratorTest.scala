package services

import java.io.ByteArrayInputStream

import org.apache.arrow.memory.RootAllocator
import org.apache.arrow.vector.{TinyIntVector, VarCharVector}
import org.apache.arrow.vector.ipc.ArrowStreamReader
import org.specs2.mutable.Specification

class PersonArrowDataGeneratorTest extends Specification {
  private val personArrowDataGenerator = new PersonArrowDataGenerator()
  private val allocator = new RootAllocator(Integer.MAX_VALUE)

  "PersonArrowDataGenerator" should {
    "generates Person in arrow format" in {
      val personInByteString = personArrowDataGenerator.generate()

      personInByteString shouldNotEqual null

      val byteArrayInputStream = new ByteArrayInputStream(personInByteString.toArray)
      val arrowReader = new ArrowStreamReader(byteArrayInputStream, allocator)
      var more = false
      do {
        more = arrowReader.loadNextBatch()
      } while (more)

      val schemaRoot = arrowReader.getVectorSchemaRoot

      val nameVector = schemaRoot.getVector("name").asInstanceOf[VarCharVector]
      val ageVector = schemaRoot.getVector("age").asInstanceOf[TinyIntVector]

      nameVector.getField.getName must beEqualTo("name")
      ageVector.getField.getName must beEqualTo("age")

      new String(nameVector.get(0)) must contain("fo")
      ageVector.get(0).toInt must lessThanOrEqualTo(24)
      ageVector.get(0).toInt must greaterThanOrEqualTo(20)
    }
  }
}
