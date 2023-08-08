import http from "@/util/axios.util";
import qs from "querystring";

export default async function handler(req, res) {
  if (req.method === "POST") {
    try {
      const response = await http(req, res).post(
        "/candidates/me/url",
        qs.stringify({ ...req.body })
      );
      res.status(response.status).json({});
    } catch (err) {
      console.log(err);
      res.status(400).json({});
    }
  }
}
