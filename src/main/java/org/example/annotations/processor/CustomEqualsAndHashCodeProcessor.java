package org.example.annotations.processor;

import org.example.annotations.EntityEqualsAndHashCode;
import org.example.annotations.Loggable;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;


/**
 * Annotation processor for generating custom `equals` and `hashCode` methods for JPA entities.
 * This processor is an alternative to the `@EqualsAndHashCode` annotation from Lombok, which is not recommended for JPA entities.
 * <p>
 * The generated `equals` method checks for equality based on the entity's ID, considering Hibernate proxies.
 * The generated `hashCode` method returns the hash code of the entity's class, considering Hibernate proxies.
 */
@SupportedAnnotationTypes("org.example.annotation.CustomEqualsAndHashCode")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CustomEqualsAndHashCodeProcessor extends AbstractProcessor {


    /**
     * Processes the annotations and generates the `equals` and `hashCode` methods for the annotated elements.
     *
     * @param annotations the set of annotations to be processed
     * @param roundEnv the environment for information about the current and prior round
     * @return true if the annotations are claimed by this processor, false otherwise
     */
    @Override
    @Loggable(logArguments = true, logReturnValue = true)
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(EntityEqualsAndHashCode.class)) {
            if (element instanceof TypeElement typeElement) {
                generateEqualsAndHashCode(typeElement);
            }
        }
        return true;
    }

    /**
     * Generates the `equals` and `hashCode` methods for the given type element.
     *
     * @param typeElement the type element for which to generate the methods
     */
    @Loggable(logArguments = true, logReturnValue = true)
    private void generateEqualsAndHashCode(TypeElement typeElement) {
        String className = typeElement.getSimpleName().toString();
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Generating equals and hashCode for " + className);

        String equalsMethod = String.format(
                """
                        @Override
                        public final boolean equals(Object o) {
                            if (this == o) return true;
                            if (o == null) return false;
                            Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
                            Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
                            if (thisEffectiveClass != oEffectiveClass) return false;
                            %s that = (%s) o;
                            return getId() != null && java.util.Objects.equals(getId(), that.getId());
                        }
                        """, className, className);

        String hashCodeMethod = """
                @Override
                public final int hashCode() {
                    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
                }
                """;

        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, equalsMethod);
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, hashCodeMethod);
    }
}
