import http from "@/util/axios.util";

export default async function handler(req, res) {
  switch (req.method) {
    case "GET":
      findJobById(req, res);
      break;
    default:
      break;
  }
}

async function findJobById(req, res) {
  try {
    const response = await http(req, res).get(`/jobs/${req.query.id}`);
    res.status(response.status).json(response.data);
  } catch (err) {
    res.status(400).json({});
  }
}
