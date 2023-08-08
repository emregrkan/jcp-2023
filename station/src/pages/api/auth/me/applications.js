import http from "@/util/axios.util";

export default async function handler(req, res) {
  try {
    const response = await http(req, res).get("/candidates/me/applications");
    res.status(response.status).json(response.data);
  } catch (err) {
    console.log(err);
    res.status(400).json({});
  }
}
