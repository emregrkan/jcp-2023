import http from "@/util/axios.util";

export default async function handler(req, res) {
  switch (req.method) {
    case "GET":
      getDepartments(req, res);
      break;
    default:
      break;
  }
}

async function getDepartments(req, res) {
  try {
    const response = await http(req, res).get("/departments");
    res.status(response.status).json(response.data);
  } catch (err) {
    res.status(400).json({});
  }
}
