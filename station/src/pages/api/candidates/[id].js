import http from "@/util/axios.util";

export default async function handler(req, res) {
  switch (req.method) {
    case "GET":
      findCandidateById(req, res);
      break;
    default:
      break;
  }
}

async function findCandidateById(req, res) {
  try {
    const response = await http(req, res).get(`/candidates/${req.query.id}`);
    res.status(response.status).json(response.data);
  } catch (error) {
    res.status(400).json({});
  }
}
