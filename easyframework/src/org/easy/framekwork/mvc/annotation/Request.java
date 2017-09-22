package org.easy.framekwork.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Request {

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Get {

		public String value();

		public String desc() default "";

		public String version() default "1.0";
	}

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Post {

		public String value();

		public String desc() default "";

		public String version() default "1.0";
	}

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Put {

		public String value();

		public String desc() default "";

		public String version() default "1.0";
	}

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Delete {

		public String value();

		public String desc() default "";

		public String version() default "1.0";
	}
}
