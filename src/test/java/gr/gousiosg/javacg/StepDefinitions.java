package gr.gousiosg.javacg;

import gr.gousiosg.javacg.stat.JCallGraph;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class StepDefinitions {
    private final JARBuilder jarBuilder;
    private String result;

    public StepDefinitions() throws IOException {
        jarBuilder = new JARBuilder();
    }

    @Given("^I have the class \"([^\"]*)\" with code:$")
    public void i_have_the_class_with_code(String className, String classCode) throws Exception {
        jarBuilder.add(className, classCode);
    }

    @When("^I run the analyze$")
    public void i_analyze_it() throws Exception {
        File jarFile = jarBuilder.build();

        PrintStream oldOut = System.out;
        ByteArrayOutputStream resultBuffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(resultBuffer));
        JCallGraph.main(new String[]{jarFile.getPath()});
        System.setOut(oldOut);

        result = resultBuffer.toString();
    }

    @Then("^the result should contain:$")
    public void the_result_should_contain(String line) throws Exception {
        if (result.contains(line)) {
            // OK
        } else {
            System.err.println(result);
            throw new RuntimeException("Cannot found: " + line);
        }
    }
}
