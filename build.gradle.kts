import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	war
	kotlin("jvm") version "1.4.32"
	kotlin("plugin.spring") version "1.4.32"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	runtimeOnly("com.h2database:h2")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
		testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}
	implementation("org.thymeleaf.extras:thymeleaf-extras-java8time:3.0.4.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	
	testImplementation("org.assertj", "assertj-core", "3.19.0")
	testImplementation("io.mockk:mockk:1.11.0")
	testImplementation("com.ninja-squad:springmockk:3.0.1")
	testImplementation("commons-io:commons-io:2.8.0")
	testImplementation("org.dbunit:dbunit:2.7.0")
	testImplementation("com.github.springtestdbunit:spring-test-dbunit:1.3.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

sourceSets {
	test {
		//assertFileEqualsで読み込むファイルを$buildDir/classes/kotlin/testを基準パスとするために
		// test/resourcesのファイルを$buildDir/classes/kotlin/testに出力するように変更
		output.resourcesDir = File("$buildDir/classes/kotlin/test")
	}
}
