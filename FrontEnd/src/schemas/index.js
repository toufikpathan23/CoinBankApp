import * as Yup from "yup";
import YupPassword from "yup-password";
YupPassword(Yup);

const phoneRegExp =
  /^(\+?\d{0,4})?\s?-?\s?(\(?\d{3}\)?)\s?-?\s?(\(?\d{3}\)?)\s?-?\s?(\(?\d{4}\)?)?$/;

export const netBankingSchema = Yup.object({
  username: Yup.string().min(2).max(25).required("username required"),
  accno: Yup.number().min(1).required("account number required"),
  password: Yup.string()
    .min(6, "password must be 6 or more character")
    .required("password required")
    .minLowercase(1, "password must contain at least 1 lower case letter")
    .minUppercase(1, "password must contain at least 1 upper case letter")
    .minNumbers(1, "password must contain at least 1 number")
    .minSymbols(1, "password must contain at least 1 special character"),

  confirmpassword: Yup.string()
    .required("confirm password required")
    .oneOf([Yup.ref("password"), null], "password mismatch"),
});
