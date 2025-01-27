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

@SupportedAnnotationTypes("org.example.annotation.CustomEqualsAndHashCode")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CustomEqualsAndHashCodeProcessor extends AbstractProcessor {

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

        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, equalsMethod);
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, hashCodeMethod);
    }
}
