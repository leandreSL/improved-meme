package TP1;

public class Calculator implements Calculator_itf {
	@Override
	public int plus(int lhs, int rhs) {
		return lhs + rhs;
	}

	@Override
	public int minus(int lhs, int rhs) {
		return lhs - rhs;
	}

	@Override
	public double divide(int top, int divisor) throws Exception {
		return (double) top / (double) divisor;
	}

	@Override
	public int multiply(int lhs, int rhs) {
		return lhs * rhs;
	}

}
