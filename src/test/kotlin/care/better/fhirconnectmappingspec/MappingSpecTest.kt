/* Copyright 2022 Better Ltd (www.better.care)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package care.better.fhirconnectmappingspec

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import jakarta.xml.bind.ValidationException
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File

/**
 * Testing and keeping track of format schemas for Model and Contextual Mappings and their compliance.
 */
class MappingSpecTest {
    private val mapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule()).enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)

    companion object {
        @JvmStatic
        fun testModelMappingsV0_0_2(): Array<File> = File(javaClass.classLoader.getResource("mapping-files-spec/v0-0-2/model-mappings").file).listFiles()
        @JvmStatic
        fun testContextualMappingsV0_0_2(): Array<File> = File(javaClass.classLoader.getResource("mapping-files-spec/v0-0-2/contextual-mappings").file).listFiles()

        @JvmStatic
        fun testModelMappingsV0_1_0(): Array<File> = File(javaClass.classLoader.getResource("mapping-files-spec/v0-1-0/model-mappings").file).listFiles()
        @JvmStatic
        fun testContextualMappingsV0_1_0(): Array<File> = File(javaClass.classLoader.getResource("mapping-files-spec/v0-1-0/contextual-mappings").file).listFiles()

        @JvmStatic
        fun testModelMappingsV0_2_0(): Array<File> = File(javaClass.classLoader.getResource("mapping-files-spec/v0-2-0/model-mappings").file).listFiles()
        @JvmStatic
        fun testContextualMappingsV0_2_0(): Array<File> = File(javaClass.classLoader.getResource("mapping-files-spec/v0-2-0/contextual-mappings").file).listFiles()
    }

    private val v0_0_2_modelMappingSchema = File(javaClass.classLoader.getResource("mapping-files-spec/v0-0-2/model-mapping.schema.json").file)
    private val v0_0_2_contextualMappingSchema = File(javaClass.classLoader.getResource("mapping-files-spec/v0-0-2/contextual-mapping.schema.json").file)

    private val v0_1_0_modelMappingSchema = File(javaClass.classLoader.getResource("mapping-files-spec/v0-1-0/model-mapping.schema.json").file)
    private val v0_1_0_contextualMappingSchema = File(javaClass.classLoader.getResource("mapping-files-spec/v0-1-0/contextual-mapping.schema.json").file)

    private val v0_2_0_modelMappingSchema = File(javaClass.classLoader.getResource("mapping-files-spec/v0-2-0/model-mapping.schema.json").file)
    private val v0_2_0_contextualMappingSchema = File(javaClass.classLoader.getResource("mapping-files-spec/v0-2-0/contextual-mapping.schema.json").file)

    @ParameterizedTest(name = "Validate Model mapping {0}")
    @MethodSource("testModelMappingsV0_0_2")
    fun `validate Model Mapping with schema v0_0_2`(input: File) {
        validate(v0_0_2_modelMappingSchema, input)
    }

    @ParameterizedTest(name = "Validate Contextual mapping {0}")
    @MethodSource("testContextualMappingsV0_0_2")
    fun `validate Contextual Mapping with schema v0_0_2`(input: File) {
        validate(v0_0_2_contextualMappingSchema, input)
    }

    @ParameterizedTest(name = "Validate Model mapping {0}")
    @MethodSource("testModelMappingsV0_1_0")
    fun `validate Model Mapping with schema v0_1_0`(input: File) {
        validate(v0_1_0_modelMappingSchema, input)
    }

    @ParameterizedTest(name = "Validate Contextual mapping {0}")
    @MethodSource("testContextualMappingsV0_1_0")
    fun `validate Contextual Mapping with schema v0_1_0`(input: File) {
        validate(v0_1_0_contextualMappingSchema, input)
    }

    @ParameterizedTest(name = "Validate Model mapping {0}")
    @MethodSource("testModelMappingsV0_2_0")
    fun `validate Model Mapping with schema v0_2_0`(input: File) {
        validate(v0_2_0_modelMappingSchema, input)
    }

    @ParameterizedTest(name = "Validate Contextual mapping {0}")
    @MethodSource("testContextualMappingsV0_2_0")
    fun `validate Contextual Mapping with schema v0_2_0`(input: File) {
        validate(v0_2_0_contextualMappingSchema, input)
    }

    private fun validate(schemaStream: File, rawYamlFile: File) {
        val factory: JsonSchemaFactory = JsonSchemaFactory.builder(JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012)).objectMapper(mapper).build()
        val schema = factory.getSchema(schemaStream.inputStream())

        val jsonNode: JsonNode = mapper.readTree(rawYamlFile)
        val validateMsg = schema.validate(jsonNode)
        if (validateMsg.isNotEmpty()) throw ValidationException(validateMsg.joinToString(";\n") { it.message })
    }

}