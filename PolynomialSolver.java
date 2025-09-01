import java.io.*;
import java.math.BigInteger;
import java.util.*;
import org.json.*;

public class PolynomialSolver {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java PolynomialSolver <testcase.json>");
            return;
        }

        String fileName = args[0];

        String jsonContent = new String(java.nio.file.Files.readAllBytes(
                java.nio.file.Paths.get(fileName)));

        JSONObject json = new JSONObject(jsonContent);
        JSONObject keys = json.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

   
        List<BigInteger> roots = new ArrayList<>();
        for (String key : json.keySet()) {
            if (key.equals("keys")) continue;
            JSONObject rootObj = json.getJSONObject(key);
            int base = Integer.parseInt(rootObj.getString("base"));
            String value = rootObj.getString("value");

            BigInteger root = new BigInteger(value, base);
            roots.add(root);
        }

        System.out.println("Roots (decimal): " + roots);

        int m = k - 1;
        List<BigInteger> chosenRoots = roots.subList(0, m);
        List<BigInteger> coefficients = buildPolynomial(chosenRoots);

        System.out.println("Polynomial coefficients (ascending order): " + coefficients);
    }

    private static List<BigInteger> buildPolynomial(List<BigInteger> roots) {
        List<BigInteger> coef = new ArrayList<>();
        coef.add(BigInteger.ONE);

        for (BigInteger root : roots) {
            List<BigInteger> newCoef = new ArrayList<>(Collections.nCopies(coef.size() + 1, BigInteger.ZERO));
            for (int i = 0; i < coef.size(); i++) {
                newCoef.set(i, newCoef.get(i).subtract(coef.get(i).multiply(root)));
                newCoef.set(i + 1, newCoef.get(i + 1).add(coef.get(i)));
            }
            coef = newCoef;
        }
        return coef;
    }
}
