import http from "@/util/axios.util";

export default async function handler(req, res) {
  console.log(req.body);
  switch (req.method) {
    case "POST":
      setJobStatus(req, res);
      break;
    default:
      break;
  }
}

async function setJobStatus(req, res) {
  try {
    const response = await http(req, res).post(
      `/jobs/${req.query.id}/status`,
      req.body
    );
    res.status(response.status).json(response.data);
  } catch (error) {
    res.status(400).json({});
  }
}
